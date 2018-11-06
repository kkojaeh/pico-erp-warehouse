package pico.erp.warehouse.transaction.request

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.ItemId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.TransactionTypeKind
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
class TransactionRequestQuerySpec extends Specification {

  def inboundRequestId = TransactionRequestId.from("create")

  def outboundRequestId = TransactionRequestId.from("outbound")

  static def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def requestItemId = TransactionRequestItemId.from("item")

  static def itemId = ItemId.from("item-1")

  def setup() {
    transactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: inboundRequestId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    transactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    transactionRequestItemService.create(
      new TransactionRequestItemRequests.CreateRequest(
        id: requestItemId,
        requestId: inboundRequestId,
        itemId: itemId,
        quantity: 20
      )
    )
  }

  @Autowired
  TransactionRequestQuery transactionRequestQuery

  @Autowired
  TransactionRequestService transactionRequestService

  @Autowired
  TransactionRequestItemService transactionRequestItemService

  def "카테고리를 지정하지 않은 품목도 조회된다"() {
    expect:
    def page = transactionRequestQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                                                      | pageable               || totalElements
    new TransactionRequestView.Filter(type: TransactionTypeKind.OUTBOUND)          | new PageRequest(0, 10) || 1
    new TransactionRequestView.Filter(itemId: itemId, relatedCompanyId: companyId) | new PageRequest(0, 10) || 1
    new TransactionRequestView.Filter()                                            | new PageRequest(0, 10) || 2
  }

}
