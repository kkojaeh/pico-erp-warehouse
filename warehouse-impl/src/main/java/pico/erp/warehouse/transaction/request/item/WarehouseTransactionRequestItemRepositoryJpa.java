package pico.erp.warehouse.transaction.request.item;

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
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;


@Repository
interface WarehouseTransactionRequestItemEntityRepository extends
  CrudRepository<WarehouseTransactionRequestItemEntity, WarehouseTransactionRequestItemId> {

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM WarehouseTransactionRequestItem i WHERE i.requestId = :requestId AND i.itemId = :itemId")
  boolean exists(@Param("requestId") WarehouseTransactionRequestId requestId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT i FROM WarehouseTransactionRequestItem i WHERE i.requestId = :requestId")
  Stream<WarehouseTransactionRequestItemEntity> findAllBy(
    @Param("requestId") WarehouseTransactionRequestId requestId);

}

@Repository
@Transactional
public class WarehouseTransactionRequestItemRepositoryJpa implements
  WarehouseTransactionRequestItemRepository {

  @Autowired
  private WarehouseTransactionRequestItemEntityRepository repository;

  @Autowired
  private WarehouseTransactionRequestItemMapper mapper;

  @Override
  public WarehouseTransactionRequestItem create(@NotNull WarehouseTransactionRequestItem domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionRequestItemId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionRequestItemId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionRequestId requestId, ItemId itemId) {
    return repository.exists(requestId, itemId);
  }

  @Override
  public Stream<WarehouseTransactionRequestItem> findAllBy(
    WarehouseTransactionRequestId requestId) {
    return repository
      .findAllBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionRequestItem> findBy(
    @NotNull WarehouseTransactionRequestItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionRequestItem domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
