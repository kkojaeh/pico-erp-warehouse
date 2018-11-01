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
import pico.erp.warehouse.transaction.request.TransactionRequestId;


@Repository
interface TransactionRequestItemEntityRepository extends
  CrudRepository<TransactionRequestItemEntity, TransactionRequestItemId> {

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM TransactionRequestItem i WHERE i.requestId = :requestId AND i.itemId = :itemId")
  boolean exists(@Param("requestId") TransactionRequestId requestId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT i FROM TransactionRequestItem i WHERE i.requestId = :requestId")
  Stream<TransactionRequestItemEntity> findAllBy(
    @Param("requestId") TransactionRequestId requestId);

}

@Repository
@Transactional
public class TransactionRequestItemRepositoryJpa implements
  TransactionRequestItemRepository {

  @Autowired
  private TransactionRequestItemEntityRepository repository;

  @Autowired
  private TransactionRequestItemMapper mapper;

  @Override
  public TransactionRequestItem create(@NotNull TransactionRequestItem domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull TransactionRequestItemId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull TransactionRequestItemId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(TransactionRequestId requestId, ItemId itemId) {
    return repository.exists(requestId, itemId);
  }

  @Override
  public Stream<TransactionRequestItem> findAllBy(
    TransactionRequestId requestId) {
    return repository
      .findAllBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<TransactionRequestItem> findBy(
    @NotNull TransactionRequestItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull TransactionRequestItem domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
