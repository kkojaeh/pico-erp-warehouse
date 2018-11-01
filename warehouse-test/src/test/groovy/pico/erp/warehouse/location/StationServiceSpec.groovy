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
import pico.erp.warehouse.location.station.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class StationServiceSpec extends Specification {

  @Autowired
  StationService warehouseStationService

  def warehouseSiteId = SiteId.from("A1")

  def warehouseStationId = StationId.from("Z")

  def setup() {
    warehouseStationService.create(new StationRequests.CreateRequest(
      id: warehouseStationId,
      siteId: warehouseSiteId,
      name: "작업장A",
      code: StationCode.from("Z"),
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
    def exists = warehouseStationService.exists(StationId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def station = warehouseStationService.get(warehouseStationId)

    then:
    station.locationCode == LocationCode.from("A1-Z")
    station.code == StationCode.from("Z")
    station.name == "작업장A"
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseStationService.get(StationId.from("unknown"))

    then:
    thrown(StationExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseStationService.create(new StationRequests.CreateRequest(
      id: StationId.from("Z2"),
      siteId: warehouseSiteId,
      name: "작업장B",
      code: StationCode.from("Z")
    ))
    then:
    thrown(StationExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseStationService.create(new StationRequests.CreateRequest(
      id: StationId.from("Z2"),
      siteId: warehouseSiteId,
      name: "작업장B",
      code: StationCode.from("Z2")
    ))
    warehouseStationService.update(new StationRequests.UpdateRequest(
      id: StationId.from("Z2"),
      name: "작업장B",
      code: StationCode.from("Z")
    ))
    then:
    thrown(StationExceptions.CodeAlreadyExistsException)
  }

}
