package pico.erp.warehouse.transaction.request;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
interface TransactionRequestEntityRepository extends
  CrudRepository<TransactionRequestEntity, TransactionRequestId> {

  @Query("SELECT r FROM TransactionRequest r WHERE r.status = :status AND r.dueDate < :fixedDate")
  Stream<TransactionRequestEntity> findAllDueDateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
    @Param("status") TransactionRequestStatusKind status);

}

@Repository
@Transactional
public class TransactionRequestRepositoryJpa implements
  TransactionRequestRepository {

  @Autowired
  private TransactionRequestEntityRepository repository;

  @Autowired
  private TransactionRequestMapper mapper;

  @Override
  public TransactionRequest create(
    @NotNull TransactionRequest request) {
    val entity = mapper.jpa(request);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionRequestId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull TransactionRequestId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<TransactionRequestAggregator> findAggregatorBy(
    TransactionRequestId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::aggregator);
  }

  @Override
  public Stream<TransactionRequest> findAllUncommittedAt(OffsetDateTime fixedDate) {
    return repository
      .findAllDueDateBeforeThan(fixedDate, TransactionRequestStatusKind.CREATED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionRequest> findBy(@NotNull TransactionRequestId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionRequest request) {
    val entity = repository.findOne(request.getId());
    mapper.pass(mapper.jpa(request), entity);
    repository.save(entity);
  }

}
