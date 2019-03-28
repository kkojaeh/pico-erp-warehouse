package pico.erp.warehouse.pack

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.item.ItemApplication
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.TestParentApplication
import pico.erp.user.UserApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.LocationId
import pico.erp.warehouse.pack.PackExceptions
import pico.erp.warehouse.pack.PackId
import pico.erp.warehouse.pack.PackRequests
import pico.erp.warehouse.pack.PackService
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [ItemApplication, UserApplication, CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class PackServiceSpec extends Specification {

  @Autowired
  PackService warehousePackService

  def warehousePackId = PackId.from("test")

  def warehouseLocationId = LocationId.from("A-1-1-1")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def setup() {
    warehousePackService.create(new PackRequests.CreateRequest(
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
    def exists = warehousePackService.exists(PackId.from("unknown"))

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
    warehousePackService.get(PackId.from("unknown"))

    then:
    thrown(PackExceptions.NotFoundException)
  }

  def "LOT 를 포장 지정한다"() {
    when:
    warehousePackService.pack(
      new PackRequests.PackRequest(
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
      new PackRequests.PutRequest(
        id: warehousePackId,
        locationId: warehouseLocationId
      )
    )
    def pack = warehousePackService.get(warehousePackId)

    then:
    pack.locationId == warehouseLocationId
  }

}
