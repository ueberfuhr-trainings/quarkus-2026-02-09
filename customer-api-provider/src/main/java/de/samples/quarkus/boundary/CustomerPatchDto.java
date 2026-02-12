package de.samples.quarkus.boundary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerPatchDto {

  @ValidState
  private String state;

}
