package pico.erp.warehouse.transaction.order

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
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
class TransactionOrderQuerySpec extends Specification {

  def inboundOrderId = TransactionOrderId.from("create")

  def outboundOrderId = TransactionOrderId.from("outbound")

  static def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def orderItemId = TransactionOrderItemId.from("item")

  static def itemId = ItemId.from("item-1")

  def setup() {
    transactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: inboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.INBOUND,
        transactionCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    transactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        transactionCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    transactionOrderItemService.create(
      new TransactionOrderItemRequests.CreateRequest(
        id: orderItemId,
        orderId: inboundOrderId,
        itemId: itemId,
        quantity: 20
      )
    )
  }

  @Autowired
  TransactionOrderQuery transactionOrderQuery

  @Autowired
  TransactionOrderService transactionOrderService

  @Autowired
  TransactionOrderItemService transactionOrderItemService

  def "검색 조건을 변경하여 검색"() {
    expect:
    def page = transactionOrderQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                                                        | pageable               || totalElements
    new TransactionOrderView.Filter(type: TransactionTypeKind.OUTBOUND)              | new PageRequest(0, 10) || 1
    new TransactionOrderView.Filter(itemId: itemId, transactionCompanyId: companyId) | new PageRequest(0, 10) || 1
    new TransactionOrderView.Filter()                                                | new PageRequest(0, 10) || 2
  }

}
