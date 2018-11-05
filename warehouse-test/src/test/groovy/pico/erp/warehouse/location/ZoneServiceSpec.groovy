package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.LocationCode
import pico.erp.warehouse.location.site.SiteId
import pico.erp.warehouse.location.zone.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ZoneServiceSpec extends Specification {

  @Autowired
  ZoneService warehouseZoneService

  def warehouseSiteId = SiteId.from("A1")

  def warehouseZoneId = ZoneId.from("Z")

  def setup() {
    warehouseZoneService.create(new ZoneRequests.CreateRequest(
      id: warehouseZoneId,
      siteId: warehouseSiteId,
      code: ZoneCode.from("Z"),
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
    def exists = warehouseZoneService.exists(ZoneId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def zone = warehouseZoneService.get(warehouseZoneId)

    then:
    zone.locationCode == LocationCode.from("A1-Z")
    zone.code == ZoneCode.from("Z")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseZoneService.get(ZoneId.from("unknown"))

    then:
    thrown(ZoneExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseZoneService.create(new ZoneRequests.CreateRequest(
      id: ZoneId.from("Z2"),
      siteId: warehouseSiteId,
      code: ZoneCode.from("Z")
    ))
    then:
    thrown(ZoneExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseZoneService.create(new ZoneRequests.CreateRequest(
      id: ZoneId.from("Z2"),
      siteId: warehouseSiteId,
      code: ZoneCode.from("Z2")
    ))
    warehouseZoneService.update(new ZoneRequests.UpdateRequest(
      id: ZoneId.from("Z2"),
      code: ZoneCode.from("Z")
    ))
    then:
    thrown(ZoneExceptions.CodeAlreadyExistsException)
  }

}