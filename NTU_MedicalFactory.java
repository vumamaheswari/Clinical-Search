/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nutch.ntunlp;

// Commons Logging imports
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// Hadoop imports
import org.apache.hadoop.conf.Configuration;

// Nutch imports
import org.apache.nutch.plugin.Extension;
import org.apache.nutch.plugin.PluginRepository;


/**
 * A factory for retrieving {@link Summarizer} extensions.
 * 
 * @author J&eacute;r&ocirc;me Charron
 */
public class NTU_MedicalFactory {

  /** My logger */
  public final static Log LOG = LogFactory.getLog(NTU_MedicalFactory.class);

  /** The first available {@link Summarizer} */
  private NTU_Medical ntunlp_instance = null;
  
  
  public NTU_MedicalFactory(Configuration conf) {
    try {
      Extension[] extensions = PluginRepository
                                    .get(conf)
                                    .getExtensionPoint(NTU_Medical.X_POINT_ID)
                                    .getExtensions();
      ntunlp_instance = (NTU_Medical) extensions[1].getExtensionInstance();
      if (LOG.isInfoEnabled()) {
        LOG.info("Using the first ntunlp_instance extension found: " +
                 extensions[1].getId());
      }
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) { LOG.warn(e.toString()); }
    }
  }

  /**
   * Get the first available {@link Summarizer} extension.
   * @return the first available {@link Summarizer} extension, or
   *         <code>null</code> if none available.
   */
  public NTU_Medical getNTU_Medical() {
    return ntunlp_instance;
  }

} 
