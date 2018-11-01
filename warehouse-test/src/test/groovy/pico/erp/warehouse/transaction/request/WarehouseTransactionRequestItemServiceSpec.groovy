package pico.erp.warehouse.transaction.request

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestRequests
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestService
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemExceptions
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemRequests
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionRequestItemServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionRequestService warehouseTransactionRequestService

  @Autowired
  WarehouseTransactionRequestItemService warehouseTransactionRequestItemService

  def requestId = WarehouseTransactionRequestId.from("create")

  def requestItemId = WarehouseTransactionRequestItemId.from("item")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionRequestService.create(
      new WarehouseTransactionRequestRequests.CreateRequest(
        id: requestId,
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
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )

    def item = warehouseTransactionRequestItemService.get(requestItemId)


    then:
    item.itemId == itemId
    item.quantity == 20
    item.requestId == requestId
    item.id == requestItemId
  }

  def "동일한 입/출고요청에 동일한 품목을 추가할 수 없다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )

    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: WarehouseTransactionRequestItemId.from("item-2"),
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )


    then:
    thrown(WarehouseTransactionRequestItemExceptions.AlreadyExistsException)
  }

  def "입/출고요청 품목을 수정한다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemService.update(
      new WarehouseTransactionRequestItemRequests.UpdateRequest(
        id: requestItemId,
        quantity: 10
      )
    )
    def item = warehouseTransactionRequestItemService.get(requestItemId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionRequestItemService.delete(
      new WarehouseTransactionRequestItemRequests.DeleteRequest(
        id: requestItemId
      )
    )

    then:
    warehouseTransactionRequestItemService.getAll(requestId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목은 생성 할 수 없다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: WarehouseTransactionRequestItemId.from("item-2"),
        requestId: requestId,
        itemId: ItemId.from("item-2"),
        quantity: 20
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목은 수정 할 수 없다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemService.update(
      new WarehouseTransactionRequestItemRequests.UpdateRequest(
        id: requestItemId,
        quantity: 10
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목은 삭제 할 수 없다"() {
    when:
    warehouseTransactionRequestItemService.create(
      new WarehouseTransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    warehouseTransactionRequestService.commit(
      new WarehouseTransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    warehouseTransactionRequestItemService.delete(
      new WarehouseTransactionRequestItemRequests.DeleteRequest(
        id: requestItemId
      )
    )
    then:
    thrown(WarehouseTransactionRequestItemExceptions.CannotDeleteException)
  }

}
