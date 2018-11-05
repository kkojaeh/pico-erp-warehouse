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
import pico.erp.warehouse.location.bay.BayId
import pico.erp.warehouse.location.level.*
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class LevelServiceSpec extends Specification {

  @Autowired
  LevelService warehouseLevelService

  def warehouseBayId = BayId.from("A-1-1")

  def warehouseLevelId = LevelId.from("A-1-1-20")

  def setup() {
    warehouseLevelService.create(new LevelRequests.CreateRequest(
      id: warehouseLevelId,
      bayId: warehouseBayId,
      code: LevelCode.from(20),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = warehouseLevelService.exists(warehouseLevelId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = warehouseLevelService.exists(LevelId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def rack = warehouseLevelService.get(warehouseLevelId)

    then:
    rack.locationCode == LocationCode.from("A1-A-01-01-20")
    rack.code == LevelCode.from(20)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    warehouseLevelService.get(LevelId.from("unknown"))

    then:
    thrown(LevelExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    warehouseLevelService.create(new LevelRequests.CreateRequest(
      id: LevelId.from("A-1-1-19"),
      bayId: warehouseBayId,
      code: LevelCode.from(20)
    ))
    then:
    thrown(LevelExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    warehouseLevelService.create(new LevelRequests.CreateRequest(
      id: LevelId.from("A-1-1-19"),
      bayId: warehouseBayId,
      code: LevelCode.from(19)
    ))
    warehouseLevelService.update(new LevelRequests.UpdateRequest(
      id: LevelId.from("A-1-1-19"),
      code: LevelCode.from(20)
    ))
    then:
    thrown(LevelExceptions.CodeAlreadyExistsException)
  }

}
