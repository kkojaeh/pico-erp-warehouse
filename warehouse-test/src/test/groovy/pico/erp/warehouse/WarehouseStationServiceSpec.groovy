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
import pico.erp.warehouse.location.station.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseStationServiceSpec extends Specification {

  @Autowired
  WarehouseStationService warehouseStationService

  def warehouseSiteId = WarehouseSiteId.from("A1")

  def warehouseStationId = WarehouseStationId.from("Z")

  def setup() {
    warehouseStationService.create(new WarehouseStationRequests.CreateRequest(
      id: warehouseStationId,
      siteId: warehouseSiteId,
      code: WarehouseStationCode.from("Z"),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseStationService.exists(warehouseStationId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseStationService.exists(WarehouseStationId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def zone = warehouseStationService.get(warehouseStationId)

    then:
    zone.locationCode == WarehouseLocationCode.from("A1-Z")
    zone.code == WarehouseStationCode.from("Z")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseStationService.get(WarehouseStationId.from("unknown"))

    then:
    thrown(WarehouseStationExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseStationService.create(new WarehouseStationRequests.CreateRequest(
      id: WarehouseStationId.from("Z2"),
      siteId: warehouseSiteId,
      code: WarehouseStationCode.from("Z")
    ))
    then:
    thrown(WarehouseStationExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseStationService.create(new WarehouseStationRequests.CreateRequest(
      id: WarehouseStationId.from("Z2"),
      siteId: warehouseSiteId,
      code: WarehouseStationCode.from("Z2")
    ))
    warehouseStationService.update(new WarehouseStationRequests.UpdateRequest(
      id: WarehouseStationId.from("Z2"),
      code: WarehouseStationCode.from("Z")
    ))
    then:
    thrown(WarehouseStationExceptions.CodeAlreadyExistsException)
  }

}
