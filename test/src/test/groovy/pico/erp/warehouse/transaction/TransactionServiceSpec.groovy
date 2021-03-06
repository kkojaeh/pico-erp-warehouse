package pico.erp.warehouse.transaction

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionId
import pico.erp.warehouse.transaction.TransactionRequests
import pico.erp.warehouse.transaction.TransactionService
import pico.erp.warehouse.transaction.TransactionTypeKind
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class TransactionServiceSpec extends Specification {

  @Autowired
  TransactionService warehouseTransactionService

  def inboundId = TransactionId.from("create")

  def outboundId = TransactionId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def setup() {
  }

  def "입고를 처리 한다"() {
    when:

    def inbounded = warehouseTransactionService.create(new TransactionRequests.CreateRequest(
      id: inboundId,
      itemLotId: itemLotId,
      quantity: 20,
      type: TransactionTypeKind.INBOUND,
      transactionCompanyId: companyId,
      stationId: stationId
    ))

    println inbounded

    then:
    inbounded.itemLotId == itemLotId
    inbounded.quantity == 20
    inbounded.type == TransactionTypeKind.INBOUND
    inbounded.transactionCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "출고를 처리 한다"() {
    when:
    def outbounded = warehouseTransactionService.create(new TransactionRequests.CreateRequest(
      id: outboundId,
      itemLotId: itemLotId,
      quantity: 20,
      type: TransactionTypeKind.OUTBOUND,
      transactionCompanyId: companyId,
      stationId: stationId
    ))

    println outbounded

    then:
    outbounded.itemLotId == itemLotId
    outbounded.quantity == 20
    outbounded.type == TransactionTypeKind.OUTBOUND
    outbounded.transactionCompanyId == companyId
    outbounded.stationId == stationId
  }

}
