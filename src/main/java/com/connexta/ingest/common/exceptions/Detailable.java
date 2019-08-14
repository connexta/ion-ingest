/*
 * Copyright (c) 2019 Connexta, LLC
 *
 * Released under the GNU Lesser General Public License version 3; see
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */
package com.connexta.ingest.common.exceptions;

import java.util.Collections;
import java.util.List;

/**
 * Exceptions can implement this interface to provide detail information that should be added to
 * error responses that are automatically generated by Spring or extends the {@link
 * DetailedResponseStatusException} exception directly. They could also simply annotate their
 * exceptions with {@Link DetailedResponseStatus}
 */
public interface Detailable {
  /**
   * Gets a more specific code for the error that should be included in the resulting error message
   * (defaults to <code>-1</code>).
   *
   * @return a more specific code for the error or <code>-1</code> if no specific code is available
   */
  default int getCode() {
    return -1;
  }

  /**
   * Gets additional details information about the error that should be included in the resulting
   * error message (defaults to none).
   *
   * @return additional information about the error
   */
  default List<String> getDetails() {
    return Collections.emptyList();
  }
}
