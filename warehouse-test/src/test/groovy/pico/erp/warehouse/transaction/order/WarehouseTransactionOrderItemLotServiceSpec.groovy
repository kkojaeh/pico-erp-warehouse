package pico.erp.warehouse.transaction.order

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.ItemId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.station.WarehouseStationId
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderRequests
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderService
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemRequests
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemService
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLotExceptions
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLotId
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLotRequests
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLotService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionOrderItemLotServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionOrderService warehouseTransactionOrderService

  @Autowired
  WarehouseTransactionOrderItemService warehouseTransactionOrderItemService

  @Autowired
  WarehouseTransactionOrderItemLotService warehouseTransactionOrderItemLotService

  def orderId = WarehouseTransactionOrderId.from("create")

  def orderItemId = WarehouseTransactionOrderItemId.from("item")

  def orderItemLotId = WarehouseTransactionOrderItemLotId.from("item-lot")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: orderId,
        dueDate: dueDate,
        type: WarehouseTransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
  }

  def "입/출고요청에 품목 LOT를 추가한다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    def itemLot = warehouseTransactionOrderItemLotService.get(orderItemLotId)


    then:
    itemLot.itemLotId == itemLotId
    itemLot.quantity == 20
    itemLot.orderItemId == orderItemId
    itemLot.id == orderItemLotId
  }

  def "동일한 입/출고요청에 동일한 품목 LOT 를 추가할 수 없다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: WarehouseTransactionOrderItemLotId.from("other"),
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    then:
    thrown(WarehouseTransactionOrderItemLotExceptions.AlreadyExistsException)
  }


  def "입/출고요청 품목 LOT 를 수정한다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemLotService.update(
      new WarehouseTransactionOrderItemLotRequests.UpdateRequest(
        id: orderItemLotId,
        quantity: 10
      )
    )
    def item = warehouseTransactionOrderItemLotService.get(orderItemLotId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemLotService.delete(
      new WarehouseTransactionOrderItemLotRequests.DeleteRequest(
        id: orderItemLotId,
      )
    )

    then:
    warehouseTransactionOrderItemLotService.getAll(orderItemId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목 LOT 는 생성 할 수 없다"() {
    when:
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemLotExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 수정 할 수 없다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.update(
      new WarehouseTransactionOrderItemLotRequests.UpdateRequest(
        id: orderItemLotId,
        quantity: 10
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemLotExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 삭제 할 수 없다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new WarehouseTransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.delete(
      new WarehouseTransactionOrderItemLotRequests.DeleteRequest(
        id: orderItemLotId,
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemLotExceptions.CannotDeleteException)
  }

}
