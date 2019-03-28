package pico.erp.warehouse.location

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.item.ItemApplication
import pico.erp.shared.TestParentApplication
import pico.erp.user.UserApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.site.SiteId
import pico.erp.warehouse.location.zone.*
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [ItemApplication, UserApplication, CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ZoneServiceSpec extends Specification {

  @Autowired
  ZoneService zoneService

  def siteId = SiteId.from("A1")

  def zoneId = ZoneId.from("Z")

  def setup() {
    zoneService.create(new ZoneRequests.CreateRequest(
      id: zoneId,
      siteId: siteId,
      code: ZoneCode.from("Z"),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = zoneService.exists(zoneId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = zoneService.exists(ZoneId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def zone = zoneService.get(zoneId)

    then:
    zone.locationCode == LocationCode.from("A1-Z")
    zone.code == ZoneCode.from("Z")
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    zoneService.get(ZoneId.from("unknown"))

    then:
    thrown(ZoneExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    zoneService.create(new ZoneRequests.CreateRequest(
      id: ZoneId.from("Z2"),
      siteId: siteId,
      code: ZoneCode.from("Z")
    ))
    then:
    thrown(ZoneExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    zoneService.create(new ZoneRequests.CreateRequest(
      id: ZoneId.from("Z2"),
      siteId: siteId,
      code: ZoneCode.from("Z2")
    ))
    zoneService.update(new ZoneRequests.UpdateRequest(
      id: ZoneId.from("Z2"),
      code: ZoneCode.from("Z")
    ))
    then:
    thrown(ZoneExceptions.CodeAlreadyExistsException)
  }

}
