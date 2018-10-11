package pico.erp.warehouse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.WarehouseLocationCode
import pico.erp.warehouse.location.site.WarehouseSiteId
import pico.erp.warehouse.location.zone.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseZoneServiceSpec extends Specification {

  @Autowired
  WarehouseZoneService warehouseZoneService

  def warehouseSiteId = WarehouseSiteId.from("A1")

  def warehouseZoneId = WarehouseZoneId.from("Z")

  def setup() {
    warehouseZoneService.create(new WarehouseZoneRequests.CreateRequest(
      id: warehouseZoneId,
      siteId: warehouseSiteId,
      code: WarehouseZoneCode.from("Z"),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseZoneService.exists(warehouseZoneId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseZoneService.exists(WarehouseZoneId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def zone = warehouseZoneService.get(warehouseZoneId)

    then:
    zone.locationCode == WarehouseLocationCode.from("A1-Z")
    zone.code == WarehouseZoneCode.from("Z")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseZoneService.get(WarehouseZoneId.from("unknown"))

    then:
    thrown(WarehouseZoneExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseZoneService.create(new WarehouseZoneRequests.CreateRequest(
      id: WarehouseZoneId.from("Z2"),
      siteId: warehouseSiteId,
      code: WarehouseZoneCode.from("Z")
    ))
    then:
    thrown(WarehouseZoneExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseZoneService.create(new WarehouseZoneRequests.CreateRequest(
      id: WarehouseZoneId.from("Z2"),
      siteId: warehouseSiteId,
      code: WarehouseZoneCode.from("Z2")
    ))
    warehouseZoneService.update(new WarehouseZoneRequests.UpdateRequest(
      id: WarehouseZoneId.from("Z2"),
      code: WarehouseZoneCode.from("Z")
    ))
    then:
    thrown(WarehouseZoneExceptions.CodeAlreadyExistsException)
  }

}
