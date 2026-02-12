package de.samples.quarkus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerPatch {

  @ValidState
  private String state;

}
