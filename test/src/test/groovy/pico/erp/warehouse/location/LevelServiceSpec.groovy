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
import pico.erp.warehouse.location.bay.BayId
import pico.erp.warehouse.location.level.*
import spock.lang.Specification

@SpringBootTest(classes = [WarehouseApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [ItemApplication, UserApplication, CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class LevelServiceSpec extends Specification {

  @Autowired
  LevelService levelService

  def bayId = BayId.from("A-1-1")

  def levelId = LevelId.from("A-1-1-20")

  def setup() {
    levelService.create(new LevelRequests.CreateRequest(
      id: levelId,
      bayId: bayId,
      code: LevelCode.from(20),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = levelService.exists(levelId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = levelService.exists(LevelId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def rack = levelService.get(levelId)

    then:
    rack.locationCode == LocationCode.from("A1-A-01-01-20")
    rack.code == LevelCode.from(20)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    levelService.get(LevelId.from("unknown"))

    then:
    thrown(LevelExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    levelService.create(new LevelRequests.CreateRequest(
      id: LevelId.from("A-1-1-19"),
      bayId: bayId,
      code: LevelCode.from(20)
    ))
    then:
    thrown(LevelExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    levelService.create(new LevelRequests.CreateRequest(
      id: LevelId.from("A-1-1-19"),
      bayId: bayId,
      code: LevelCode.from(19)
    ))
    levelService.update(new LevelRequests.UpdateRequest(
      id: LevelId.from("A-1-1-19"),
      code: LevelCode.from(20)
    ))
    then:
    thrown(LevelExceptions.CodeAlreadyExistsException)
  }

}
