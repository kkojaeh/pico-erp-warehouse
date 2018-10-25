package pico.erp.warehouse.transaction

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestRequests
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestService
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemRequests
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemService
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLotExceptions
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLotId
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLotRequests
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLotService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionRequestItemLotServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionRequestService warehouseTransactionRequestService

  @Autowired
  WarehouseTransactionRequestItemService warehouseTransactionRequestItemService

  @Autowired
  WarehouseTransactionRequestItemLotService warehouseTransactionRequestItemLotService

  def transactionRequestId = WarehouseTransactionRequestId.from("create")

  def transactionRequestItemId = WarehouseTransactionRequestItemId.from("item")

  def transactionRequestItemLotId = WarehouseTransactionRequestItemLotId.from("item-lot")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: transactionRequestId,
      dueDate: dueDate,
      type: WarehouseTransactionTypeKind.INBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: transactionRequestItemId,
        transactionRequestId: transactionRequestId,
        itemId: itemId,
        quantity: 20
      )
    )
  }

  def "입/출고요청에 품목 LOT를 추가한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    def itemLot = warehouseTransactionRequestItemLotService.get(transactionRequestItemLotId)


    then:
    itemLot.itemLotId == itemLotId
    itemLot.quantity == 20
    itemLot.transactionRequestItemId == transactionRequestItemId
    itemLot.id == transactionRequestItemLotId
  }

  def "동일한 입/출고요청에 동일한 품목 LOT 를 추가할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: WarehouseTransactionRequestItemLotId.from("other"),
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )

    then:
    thrown(WarehouseTransactionRequestItemLotExceptions.AlreadyExistsException)
  }


  def "입/출고요청 품목 LOT 를 수정한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemLotService.update(
      new WarehouseTransactionRequestItemLotRequests.UpdateRequest(
        id: transactionRequestItemLotId,
        quantity: 10
      )
    )
    def item = warehouseTransactionRequestItemLotService.get(transactionRequestItemLotId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemLotService.delete(
      new WarehouseTransactionRequestItemLotRequests.DeleteRequest(
        id: transactionRequestItemLotId,
      )
    )

    then:
    warehouseTransactionRequestItemLotService.getAll(transactionRequestItemId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목 LOT 는 생성 할 수 없다"() {
    when:
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: transactionRequestId
      )
    )
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemLotExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 수정 할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: transactionRequestId
      )
    )
    warehouseTransactionRequestItemLotService.update(
      new WarehouseTransactionRequestItemLotRequests.UpdateRequest(
        id: transactionRequestItemLotId,
        quantity: 10
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemLotExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목 LOT 는 삭제 할 수 없다"() {
    when:
    warehouseTransactionRequestItemLotService.create(
      new WarehouseTransactionRequestItemLotRequests.CreateRequest(
        id: transactionRequestItemLotId,
        transactionRequestItemId: transactionRequestItemId,
        itemLotId: itemLotId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: transactionRequestId
      )
    )
    warehouseTransactionRequestItemLotService.delete(
      new WarehouseTransactionRequestItemLotRequests.DeleteRequest(
        id: transactionRequestItemLotId,
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemLotExceptions.CannotDeleteException)
  }

}
