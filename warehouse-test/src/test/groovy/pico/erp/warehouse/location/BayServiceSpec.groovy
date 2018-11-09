package pico.erp.warehouse.location

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.bay.*
import pico.erp.warehouse.location.rack.RackId
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class BayServiceSpec extends Specification {

  @Autowired
  BayService bayService

  def rackId = RackId.from("A-1")

  def bayId = BayId.from("A-1-99")

  def setup() {
    bayService.create(new BayRequests.CreateRequest(
      id: bayId,
      rackId: rackId,
      code: BayCode.from(99),
    ))
  }

  def "아이디로 존재하는 창고지 확인"() {
    when:
    def exists = bayService.exists(bayId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 창고지 확인"() {
    when:
    def exists = bayService.exists(BayId.from("unknown"))

    then:
    exists == false
  }

  def "아이디로 존재하는 창고지를 조회"() {
    when:
    def bay = bayService.get(bayId)

    then:
    bay.locationCode == LocationCode.from("A1-A-01-99")
    bay.code == BayCode.from(99)
  }

  def "아이디로 존재하지 않는 창고지를 조회"() {
    when:
    bayService.get(BayId.from("unknown"))

    then:
    thrown(BayExceptions.NotFoundException)
  }

  def "중복 코드를 생성하면 오류 발생"() {
    when:
    bayService.create(new BayRequests.CreateRequest(
      id: BayId.from("A-1-100"),
      rackId: rackId,
      code: BayCode.from(99)
    ))
    then:
    thrown(BayExceptions.CodeAlreadyExistsException)
  }

  def "이미 존재하는 코드로 변경하면 오류 발생"() {
    when:
    bayService.create(new BayRequests.CreateRequest(
      id: BayId.from("A-1-100"),
      rackId: rackId,
      code: BayCode.from(98)
    ))
    bayService.update(new BayRequests.UpdateRequest(
      id: BayId.from("A-1-100"),
      code: BayCode.from(99)
    ))
    then:
    thrown(BayExceptions.CodeAlreadyExistsException)
  }

}
