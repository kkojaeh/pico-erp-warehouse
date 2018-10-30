package pico.erp.warehouse.transaction;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
interface WarehouseTransactionEntityRepository extends
  CrudRepository<WarehouseTransactionEntity, WarehouseTransactionId> {

}

@Repository
@Transactional
public class WarehouseTransactionRepositoryJpa implements WarehouseTransactionRepository {

  @Autowired
  private WarehouseTransactionEntityRepository repository;

  @Autowired
  private WarehouseTransactionMapper mapper;

  @Override
  public WarehouseTransaction create(@NotNull WarehouseTransaction transaction) {
    val entity = mapper.jpa(transaction);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<WarehouseTransaction> findBy(@NotNull WarehouseTransactionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransaction rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
