package pico.erp.warehouse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.WarehouseLocationId
import pico.erp.warehouse.pack.WarehousePackExceptions
import pico.erp.warehouse.pack.WarehousePackId
import pico.erp.warehouse.pack.WarehousePackRequests
import pico.erp.warehouse.pack.WarehousePackService
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionServiceSpec extends Specification {

  @Autowired
  WarehousePackService warehousePackService

  def warehousePackId = WarehousePackId.from("test")

  def warehouseLocationId = WarehouseLocationId.from("A-1-1-1")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    warehousePackService.create(new WarehousePackRequests.CreateRequest(
      id: warehousePackId
    ))
  }

  def "아이디로 존재여부 확인"() {
    when:

    def exists = warehousePackService.exists(warehousePackId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 팩 확인"() {
    when:
    def exists = warehousePackService.exists(WarehousePackId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 팩을 조회"() {
    when:
    def pack = warehousePackService.get(warehousePackId)

    println pack

    then:

    pack.id == warehousePackId
  }

  def "아이디로 존재하지 않는 팩을 조회"() {
    when:
    warehousePackService.get(WarehousePackId.from("unknown"))

    then:
    thrown(WarehousePackExceptions.NotFoundException)
  }

  def "LOT 를 포장 지정한다"() {
    when:
    warehousePackService.pack(
      new WarehousePackRequests.PackRequest(
        id: warehousePackId,
        itemLotId: itemLotId,
        quantity: 10
      )
    )
    def pack = warehousePackService.get(warehousePackId)

    then:
    pack.itemLotId == itemLotId
    pack.quantity == 10
  }

  def "창고 위치를 지정한다"() {
    when:
    warehousePackService.put(
      new WarehousePackRequests.PutRequest(
        id: warehousePackId,
        locationId: warehouseLocationId
      )
    )
    def pack = warehousePackService.get(warehousePackId)

    then:
    pack.locationId == warehouseLocationId
  }

}
