package pico.erp.warehouse.location.station;

import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.LabeledValue;
import pico.erp.shared.Public;
import pico.erp.shared.QExtendedLabeledValue;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;
import pico.erp.warehouse.location.site.QSiteEntity;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class StationQueryJpa implements StationQuery {

  private final QStationEntity station = QStationEntity.stationEntity;

  private final QSiteEntity site = QSiteEntity.siteEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;


  @Override
  public List<? extends LabeledValuable> asLabels(String keyword, long limit) {
    val query = new JPAQuery<LabeledValue>(entityManager);

    val select = new QExtendedLabeledValue(
      station.id.value.as("value"),
      station.name.as("label"),
      station.code.value.as("subLabel"),
      site.code.value.prepend("[").append("]").append(site.name)
        .as("stamp")
    );
    query.select(select);
    query.from(station);
    query.leftJoin(site)
      .on(station.siteId.eq(site.id));
    query
      .where(station.name.likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%"))
        .or(station.code.value.likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%")))
        .and(station.deleted.ne(true)));
    query.limit(limit);
    query.orderBy(station.name.asc());
    return query.fetch();
  }
}
