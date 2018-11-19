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
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.TransactionTypeKind
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemExceptions
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemRequests
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemService
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class TransactionRequestItemServiceSpec extends Specification {

  @Autowired
  TransactionRequestService transactionRequestService

  @Autowired
  TransactionRequestItemService transactionRequestItemService

  def requestId = TransactionRequestId.from("create")

  def requestItemId = TransactionRequestItemId.from("item")

  def itemId = ItemId.from("item-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def setup() {
    def dueDate = OffsetDateTime.now().plusDays(2)
    transactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: requestId,
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
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )

    def item = transactionRequestItemService.get(requestItemId)


    then:
    item.itemId == itemId
    item.quantity == 20
    item.requestId == requestId
    item.id == requestItemId
  }

  def "동일한 입/출고요청에 동일한 품목을 추가할 수 없다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )

    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: TransactionRequestItemId.from("item-2"),
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )


    then:
    thrown(TransactionRequestItemExceptions.AlreadyExistsException)
  }

  def "입/출고요청 품목을 수정한다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    transactionRequestItemService.update(
      new TransactionRequestItemRequests.UpdateRequest(
        id: requestItemId,
        quantity: 10
      )
    )
    def item = transactionRequestItemService.get(requestItemId)

    then:
    item.quantity == 10
  }

  def "입/출고요청 품목을 삭제한다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    transactionRequestItemService.delete(
      new TransactionRequestItemRequests.DeleteRequest(
        id: requestItemId
      )
    )

    then:
    transactionRequestItemService.getAll(requestId).isEmpty() == true
  }

  def "제출된 입/출고요청 품목은 생성 할 수 없다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    transactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: TransactionRequestItemId.from("item-2"),
        requestId: requestId,
        itemId: ItemId.from("item-2"),
        quantity: 20
      )
    )
    then:
    thrown(TransactionRequestItemExceptions.CannotCreateException)
  }

  def "제출된 입/출고요청 품목은 수정 할 수 없다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    transactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    transactionRequestItemService.update(
      new TransactionRequestItemRequests.UpdateRequest(
        id: requestItemId,
        quantity: 10
      )
    )
    then:
    thrown(TransactionRequestItemExceptions.CannotUpdateException)
  }

  def "제출된 입/출고요청 품목은 삭제 할 수 없다"() {
    when:
    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: requestId,
        itemId: itemId,
        quantity: 20
      )
    )
    transactionRequestService.commit(
      new TransactionRequestRequests.CommitRequest(
        id: requestId
      )
    )
    transactionRequestItemService.delete(
      new TransactionRequestItemRequests.DeleteRequest(
        id: requestItemId
      )
    )
    then:
    thrown(TransactionRequestItemExceptions.CannotDeleteException)
  }

}
