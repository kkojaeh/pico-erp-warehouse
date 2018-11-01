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
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.station.WarehouseStationId
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderRequests
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderService
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemExceptions
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemRequests
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionOrderItemServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionOrderService warehouseTransactionOrderService

  @Autowired
  WarehouseTransactionOrderItemService warehouseTransactionOrderItemService

  def orderId = WarehouseTransactionOrderId.from("create")

  def orderItemId = WarehouseTransactionOrderItemId.from("item")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

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
  }

  def "입/출고요청에 품목을 추가한다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )

    def item = warehouseTransactionOrderItemService.get(orderItemId)


    then:
    item.itemId == itemId
    item.quantity == 20
    item.orderId == orderId
    item.id == orderItemId
  }

  def "동일한 입/출고요청에 동일한 품목을 추가할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )

    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: WarehouseTransactionOrderItemId.from("item-2"),
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )


    then:
    thrown(WarehouseTransactionOrderItemExceptions.AlreadyExistsException)
  }

  def "입/출고요청 품목을 수정한다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemService.update(
      new WarehouseTransactionOrderItemRequests.UpdateRequest(
        id: orderItemId,
        quantity: 10
      )
    )
    def item = warehouseTransactionOrderItemService.get(orderItemId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemService.delete(
      new WarehouseTransactionOrderItemRequests.DeleteRequest(
        id: orderItemId
      )
    )

    then:
    warehouseTransactionOrderItemService.getAll(orderId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목은 생성 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: WarehouseTransactionOrderItemId.from("item-2"),
        orderId: orderId,
        itemId: ItemId.from("item-2"),
        quantity: 20
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목은 수정 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.update(
      new WarehouseTransactionOrderItemRequests.UpdateRequest(
        id: orderItemId,
        quantity: 10
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목은 삭제 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new WarehouseTransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new WarehouseTransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.delete(
      new WarehouseTransactionOrderItemRequests.DeleteRequest(
        id: orderItemId
      )
    )
    then:
    thrown(WarehouseTransactionOrderItemExceptions.CannotDeleteException)
  }

}
