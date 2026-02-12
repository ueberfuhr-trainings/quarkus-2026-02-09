package de.samples.quarkus.boundary;

import de.samples.quarkus.domain.Customer;
import de.samples.quarkus.domain.CustomerState;
import de.samples.quarkus.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(config = MapStructConfig.class)
public interface CustomerDtoMapper {

  CustomerDto map(Customer source);

  Customer map(CustomerDto source);

  @ValueMapping(source = "active", target = "ACTIVE")
  @ValueMapping(source = "locked", target = "LOCKED")
  @ValueMapping(source = "disabled", target = "DISABLED")
  CustomerState mapState(String state);

  @ValueMapping(source = "ACTIVE", target = "active")
  @ValueMapping(source = "LOCKED", target = "locked")
  @ValueMapping(source = "DISABLED", target = "disabled")
  String mapState(CustomerState state);

}
