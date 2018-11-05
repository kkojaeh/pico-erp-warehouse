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
import pico.erp.warehouse.location.rack.*
import pico.erp.warehouse.location.zone.ZoneId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class RackServiceSpec extends Specification {

  @Autowired
  RackService warehouseRackService

  def warehouseZoneId = ZoneId.from("A")

  def warehouseRackId = RackId.from("A-99")

  def setup() {
    warehouseRackService.create(new RackRequests.CreateRequest(
      id: warehouseRackId,
      zoneId: warehouseZoneId,
      code: RackCode.from(99),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseRackService.exists(warehouseRackId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseRackService.exists(RackId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def rack = warehouseRackService.get(warehouseRackId)

    then:
    rack.locationCode == LocationCode.from("A1-A-99")
    rack.code == RackCode.from(99)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseRackService.get(RackId.from("unknown"))

    then:
    thrown(RackExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseRackService.create(new RackRequests.CreateRequest(
      id: RackId.from("A-100"),
      zoneId: warehouseZoneId,
      code: RackCode.from(99),
    ))
    then:
    thrown(RackExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseRackService.create(new RackRequests.CreateRequest(
      id: RackId.from("A-100"),
      zoneId: warehouseZoneId,
      code: RackCode.from(98),
    ))
    warehouseRackService.update(new RackRequests.UpdateRequest(
      id: RackId.from("A-100"),
      code: RackCode.from(99),
    ))
    then:
    thrown(RackExceptions.CodeAlreadyExistsException)
  }

}
