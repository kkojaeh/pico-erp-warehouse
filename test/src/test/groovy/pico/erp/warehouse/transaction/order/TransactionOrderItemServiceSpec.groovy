package pico.erp.warehouse.transaction.order

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.ItemId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.TransactionTypeKind
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemExceptions
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemRequests
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class TransactionOrderItemServiceSpec extends Specification {

  @Autowired
  TransactionOrderService warehouseTransactionOrderService

  @Autowired
  TransactionOrderItemService warehouseTransactionOrderItemService

  def orderId = TransactionOrderId.from("create")

  def orderItemId = TransactionOrderItemId.from("item")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

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
  }

  def "입/출고요청에 품목을 추가한다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
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
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )

    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: TransactionOrderItemId.from("item-2"),
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )


    then:
    thrown(TransactionOrderItemExceptions.AlreadyExistsException)
  }

  def "입/출고요청 품목을 수정한다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemService.update(
      new TransactionOrderItemRequests.UpdateRequest(
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
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderItemService.delete(
      new TransactionOrderItemRequests.DeleteRequest(
        id: orderItemId
      )
    )

    then:
    warehouseTransactionOrderItemService.getAll(orderId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목은 생성 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: TransactionOrderItemId.from("item-2"),
        orderId: orderId,
        itemId: ItemId.from("item-2"),
        quantity: 20
      )
    )
    then:
    thrown(TransactionOrderItemExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목은 수정 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.update(
      new TransactionOrderItemRequests.UpdateRequest(
        id: orderItemId,
        quantity: 10
      )
    )
    then:
    thrown(TransactionOrderItemExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목은 삭제 할 수 없다"() {
    when:
    warehouseTransactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: orderId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionOrderService.commit(
      new TransactionOrderRequests.CommitRequest(
        id: orderId
      )
    )
    warehouseTransactionOrderItemService.delete(
      new TransactionOrderItemRequests.DeleteRequest(
        id: orderItemId
      )
    )
    then:
    thrown(TransactionOrderItemExceptions.CannotDeleteException)
  }

}
