package pico.erp.warehouse.transaction.request

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
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemRequests
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemService
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotExceptions
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotId
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotRequests
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class TransactionRequestItemLotServiceSpec extends Specification {

  @Autowired
  TransactionRequestService warehouseTransactionRequestService

  @Autowired
  TransactionRequestItemService warehouseTransactionRequestItemService

  @Autowired
  TransactionRequestItemLotService warehouseTransactionRequestItemLotService

  def requestId = TransactionRequestId.from("create")

  def requestItemId = TransactionRequestItemId.from("item")

  def requestItemLotId = TransactionRequestItemLotId.from("item-lot")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: requestId,
        dueDate: dueDate,
        type: TransactionTypeKind.INBOUND,
        transactionCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
  }

  def "입/출고요청에 품목 LOT를 추가한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    def itemLot = warehouseTransactionRequestItemLotService.get(requestItemLotId)


    then:
    itemLot.itemLotId == itemLotId
    itemLot.quantity == 20
    itemLot.requestItemId == requestItemId
    itemLot.id == requestItemLotId
  }

  def "동일한 입/출고요청에 동일한 품목 LOT 를 추가할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: TransactionRequestItemLotId.from("other"),
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    then:
    thrown(TransactionRequestItemLotExceptions.AlreadyExistsException)
  }


  def "입/출고요청 품목 LOT 를 수정한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemLotService.update(
      new TransactionRequestItemLotRequests.UpdateRequest(
        id: requestItemLotId,
        quantity: 10
      )
    )
    def item = warehouseTransactionRequestItemLotService.get(requestItemLotId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemLotService.delete(
      new TransactionRequestItemLotRequests.DeleteRequest(
        id: requestItemLotId,
      )
    )

    then:
    warehouseTransactionRequestItemLotService.getAll(requestItemId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목 LOT 는 생성 할 수 없다"() {
    when:
    warehouseTransactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    then:
    thrown(TransactionRequestItemLotExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 수정 할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemLotService.update(
      new TransactionRequestItemLotRequests.UpdateRequest(
        id: requestItemLotId,
        quantity: 10
      )
    )
    then:
    thrown(TransactionRequestItemLotExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 삭제 할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new TransactionRequestItemLotRequests.CreateRequest(
        id: requestItemLotId,
        requestItemId: requestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemLotService.delete(
      new TransactionRequestItemLotRequests.DeleteRequest(
        id: requestItemLotId,
      )
    )
    then:
    thrown(TransactionRequestItemLotExceptions.CannotDeleteException)
  }

}
