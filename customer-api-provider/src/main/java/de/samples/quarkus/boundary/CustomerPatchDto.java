package de.samples.quarkus.boundary;

import de.samples.quarkus.ValidState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerPatchDto {

  @ValidState
  private String state;

}
