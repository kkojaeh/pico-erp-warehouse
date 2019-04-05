package pico.erp.warehouse.transaction.order

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.ItemId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.TransactionTypeKind
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemRequests
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemService
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotExceptions
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotId
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotRequests
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class TransactionOrderItemLotServiceSpec extends Specification {

  @Autowired
  TransactionOrderService warehouseTransactionOrderService

  @Autowired
  TransactionOrderItemService warehouseTransactionOrderItemService

  @Autowired
  TransactionOrderItemLotService warehouseTransactionOrderItemLotService

  def orderId = TransactionOrderId.from("create")

  def orderItemId = TransactionOrderItemId.from("item")

  def orderItemLotId = TransactionOrderItemLotId.from("item-lot")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: orderId,
        dueDate: dueDate,
        type: TransactionTypeKind.INBOUND,
        transactionCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
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
      new TransactionOrderItemLotRequests.CreateRequest(
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
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    warehouseTransactionOrderItemLotService.create(
      new TransactionOrderItemLotRequests.CreateRequest(
        id: TransactionOrderItemLotId.from("other"),
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    then:
    thrown(TransactionOrderItemLotExceptions.AlreadyExistsException)
  }


  def "입/출고요청 품목 LOT 를 수정한다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemLotService.update(
      new TransactionOrderItemLotRequests.UpdateRequest(
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
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemLotService.delete(
      new TransactionOrderItemLotRequests.DeleteRequest(
        id: orderItemLotId,
      )
    )

    then:
    warehouseTransactionOrderItemLotService.getAll(orderItemId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목 LOT 는 생성 할 수 없다"() {
    when:
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.create(
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    then:
    thrown(TransactionOrderItemLotExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 수정 할 수 없다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.update(
      new TransactionOrderItemLotRequests.UpdateRequest(
        id: orderItemLotId,
        quantity: 10
      )
    )
    then:
    thrown(TransactionOrderItemLotExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 삭제 할 수 없다"() {
    when:
    warehouseTransactionOrderItemLotService.create(
      new TransactionOrderItemLotRequests.CreateRequest(
        id: orderItemLotId,
        orderItemId: orderItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemLotService.delete(
      new TransactionOrderItemLotRequests.DeleteRequest(
        id: orderItemLotId,
      )
    )
    then:
    thrown(TransactionOrderItemLotExceptions.CannotDeleteException)
  }

}
