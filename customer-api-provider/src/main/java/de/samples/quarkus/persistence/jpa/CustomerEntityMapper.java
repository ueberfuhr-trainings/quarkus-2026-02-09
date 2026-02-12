package de.samples.quarkus.persistence.jpa;

import de.samples.quarkus.domain.Customer;
import de.samples.quarkus.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface CustomerEntityMapper {

  CustomerEntity map(Customer source);

  Customer map(CustomerEntity source);

  void copy(CustomerEntity source, @MappingTarget CustomerEntity target);

  void copy(CustomerEntity source, @MappingTarget Customer target);

}
