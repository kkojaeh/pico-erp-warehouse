package pico.erp.warehouse.location

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.warehouse.TestConfig
import pico.erp.warehouse.WarehouseApplication
import pico.erp.warehouse.location.site.SiteId
import pico.erp.warehouse.location.station.*
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class StationServiceSpec extends Specification {

  @Autowired
  StationService stationService

  def siteId = SiteId.from("A1")

  def stationId = StationId.from("Z")

  def setup() {
    stationService.create(new StationRequests.CreateRequest(
      id: stationId,
      siteId: siteId,
      name: "작업장A",
      code: StationCode.from("Z"),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = stationService.exists(stationId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = stationService.exists(StationId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def station = stationService.get(stationId)

    then:
    station.locationCode == LocationCode.from("A1-Z")
    station.code == StationCode.from("Z")
    station.name == "작업장A"
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    stationService.get(StationId.from("unknown"))

    then:
    thrown(StationExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    stationService.create(new StationRequests.CreateRequest(
      id: StationId.from("Z2"),
      siteId: siteId,
      name: "작업장B",
      code: StationCode.from("Z")
    ))
    then:
    thrown(StationExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    stationService.create(new StationRequests.CreateRequest(
      id: StationId.from("Z2"),
      siteId: siteId,
      name: "작업장B",
      code: StationCode.from("Z2")
    ))
    stationService.update(new StationRequests.UpdateRequest(
      id: StationId.from("Z2"),
      name: "작업장B",
      code: StationCode.from("Z")
    ))
    then:
    thrown(StationExceptions.CodeAlreadyExistsException)
  }

}
