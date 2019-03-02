package com.sarality.dataport.file;

import com.sarality.form.ControlType;
import com.sarality.form.FormField;

/**
 * Form Field for additional information needed for Import and Export
 *
 * @author abhideep@ (Abhideep Singh)
 */
public enum SyncStatusFormField implements FormField {
  GLOBAL_ID,
  GLOBAL_VERSION,
  LOCALLY_MODIFIED,
  ;

  @Override
  public String getName() {
    return name();
  }

  @Override
  public int getViewId() {
    return 0;
  }

  @Override public ControlType getControlType() {
    return null;
  }
}
