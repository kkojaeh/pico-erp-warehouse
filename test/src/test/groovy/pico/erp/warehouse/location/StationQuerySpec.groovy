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
import pico.erp.warehouse.location.station.*
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [ItemApplication, UserApplication, CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class StationQuerySpec extends Specification {

  @Autowired
  StationService stationService

  @Autowired
  StationQuery stationQuery

  def siteId = SiteId.from("A1")

  def stationId = StationId.from("Z")

  def stationId2 = StationId.from("Y")

  def setup() {
    stationService.create(new StationRequests.CreateRequest(
      id: stationId,
      siteId: siteId,
      name: "작업장A",
      code: StationCode.from("Z"),
    ))
    stationService.create(new StationRequests.CreateRequest(
      id: stationId2,
      siteId: siteId,
      name: "작업장B",
      code: StationCode.from("Y"),
    ))
  }

  def "사용자 조회 - 조회 조건에 맞게 조회"() {
    expect:
    def list = stationQuery.asLabels(keyword, 10)
    list.size() == size


    where:
    keyword || size
    "작업장"   || 2
    "A"     || 1
    "B"     || 1
    "Z"     || 1
    "Y"     || 1
    "AB"    || 0
  }

}
