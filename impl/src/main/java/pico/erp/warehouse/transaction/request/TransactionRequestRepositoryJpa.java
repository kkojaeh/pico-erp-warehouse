package pico.erp.warehouse.transaction.request;

import java.time.LocalDateTime;
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

  @Query("SELECT COUNT(r) FROM TransactionRequest r WHERE r.createdDate >= :begin AND r.createdDate <= :end")
  long countCreatedBetween(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

  @Query("SELECT r FROM TransactionRequest r WHERE r.status = :status AND r.dueDate < :fixedDate")
  Stream<TransactionRequestEntity> findAllDueDateBeforeThan(
    @Param("fixedDate") LocalDateTime fixedDate,
    @Param("status") TransactionRequestStatusKind status);

  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM TransactionRequest r WHERE r.code = :code")
  boolean exists(@Param("code") TransactionRequestCode code);

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
  public long countCreatedBetween(LocalDateTime begin, LocalDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public void deleteBy(@NotNull TransactionRequestId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(TransactionRequestCode code) {
    return repository.exists(code);
  }

  @Override
  public boolean exists(@NotNull TransactionRequestId id) {
    return repository.existsById(id);
  }

  @Override
  public Optional<TransactionRequestAggregator> findAggregatorBy(
    TransactionRequestId id) {
    return repository.findById(id)
      .map(mapper::aggregator);
  }

  @Override
  public Stream<TransactionRequest> findAllUncommittedAt(LocalDateTime fixedDate) {
    return repository
      .findAllDueDateBeforeThan(fixedDate, TransactionRequestStatusKind.CREATED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionRequest> findBy(@NotNull TransactionRequestId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionRequest request) {
    val entity = repository.findById(request.getId()).get();
    mapper.pass(mapper.jpa(request), entity);
    repository.save(entity);
  }

}
