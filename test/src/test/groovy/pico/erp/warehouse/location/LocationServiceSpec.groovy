package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.bay.BayId
import pico.erp.warehouse.location.level.LevelCode
import pico.erp.warehouse.location.level.LevelId
import pico.erp.warehouse.location.level.LevelRequests
import pico.erp.warehouse.location.level.LevelService
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class LocationServiceSpec extends Specification {

  @Autowired
  LevelService levelService

  @Autowired
  LocationService locationService

  def bayId = BayId.from("A-1-1")

  def levelId = LevelId.from("A-1-1-20")

  def locationId = LocationId.from("A-1-1-20")

  def setup() {
    levelService.create(new LevelRequests.CreateRequest(
      id: levelId,
      bayId: bayId,
      code: LevelCode.from(20),
    ))
  }

  def "레벨을 생성하면 동일 아이디로 위치가 존재"() {
    when:

    def exists = locationService.exists(locationId)

    then:
    exists == true
  }

  def "레벨을 생성하면 동일 아이디의 위치와 값이 동일"() {
    when:
    def level = levelService.get(levelId)
    def location = locationService.get(locationId)

    then:
    level.id.value == location.id.value
    level.locationCode == location.code
  }


}