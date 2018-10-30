package pico.erp.warehouse.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.station.WarehouseStationId
import pico.erp.warehouse.transaction.WarehouseTransactionId
import pico.erp.warehouse.transaction.WarehouseTransactionRequests
import pico.erp.warehouse.transaction.WarehouseTransactionService
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionService warehouseTransactionService

  def inboundId = WarehouseTransactionId.from("create")

  def outboundId = WarehouseTransactionId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def setup() {
  }

  def "입고를 처리 한다"() {
    when:

    def inbounded = warehouseTransactionService.create(new WarehouseTransactionRequests.CreateRequest(
      id: inboundId,
      itemLotId: itemLotId,
      quantity: 20,
      type: WarehouseTransactionTypeKind.INBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    println inbounded

    then:
    inbounded.itemLotId == itemLotId
    inbounded.quantity == 20
    inbounded.type == WarehouseTransactionTypeKind.INBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "출고를 처리 한다"() {
    when:
    def outbounded = warehouseTransactionService.create(new WarehouseTransactionRequests.CreateRequest(
      id: outboundId,
      itemLotId: itemLotId,
      quantity: 20,
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    println outbounded

    then:
    outbounded.itemLotId == itemLotId
    outbounded.quantity == 20
    outbounded.type == WarehouseTransactionTypeKind.OUTBOUND
    outbounded.relatedCompanyId == companyId
    outbounded.stationId == stationId
  }

}
