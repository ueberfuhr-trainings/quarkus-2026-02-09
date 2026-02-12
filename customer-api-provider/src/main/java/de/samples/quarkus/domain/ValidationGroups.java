package de.samples.quarkus.domain;

import jakarta.validation.groups.Default;

/**
 * Validation groups for lifecycle-dependent constraints.
 * Used with {@link jakarta.validation.groups.ConvertGroup}
 * to activate the appropriate constraints depending on the operation.
 */
public interface ValidationGroups {

  /**
   * Validation group for creating new entities.
   * Extends {@link Default} so that standard constraints
   * are also validated when this group is active.
   */
  interface OnCreate extends Default {
  }

  /**
   * Validation group for updating existing entities.
   * Extends {@link Default} so that standard constraints
   * are also validated when this group is active.
   */
  interface OnUpdate extends Default {
  }

}
