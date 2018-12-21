package pico.erp.warehouse.transaction.order;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.user.group.GroupId;

@Data
@Configuration
@ConfigurationProperties("warehouse.transaction.order")
public class TransactionOrderProperties {

  List<GroupId> orderAcceptGroups;

}
