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
interface WarehouseTransactionRequestEntityRepository extends
  CrudRepository<WarehouseTransactionRequestEntity, WarehouseTransactionRequestId> {

  @Query("SELECT wtr FROM WarehouseTransactionRequest wtr WHERE wtr.status = :status AND wtr.dueDate < :fixedDate")
  Stream<WarehouseTransactionRequestEntity> findAllDueDateBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate,
    @Param("status") WarehouseTransactionRequestStatusKind status);

}

@Repository
@Transactional
public class WarehouseTransactionRequestRepositoryJpa implements
  WarehouseTransactionRequestRepository {

  @Autowired
  private WarehouseTransactionRequestEntityRepository repository;

  @Autowired
  private WarehouseTransactionRequestMapper mapper;

  @Override
  public WarehouseTransactionRequest create(
    @NotNull WarehouseTransactionRequest transactionRequest) {
    val entity = mapper.jpa(transactionRequest);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionRequestId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionRequestId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<WarehouseTransactionRequest> findAllUncommittedAt(OffsetDateTime fixedDate) {
    return repository
      .findAllDueDateBeforeThan(fixedDate, WarehouseTransactionRequestStatusKind.CREATED)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionRequest> findBy(@NotNull WarehouseTransactionRequestId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionRequest transactionRequest) {
    val entity = repository.findOne(transactionRequest.getId());
    mapper.pass(mapper.jpa(transactionRequest), entity);
    repository.save(entity);
  }

}
