/**
 * Copyright © 2014-2021 The SiteWhere Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sitewhere.rdb;

import com.sitewhere.microservice.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.rdb.spi.IRdbTenantComponent;
import com.sitewhere.spi.microservice.lifecycle.LifecycleComponentType;

/**
 * Base class for components in a tenant engine that rely on RDB connectivity.
 */
public abstract class RdbTenantComponent extends TenantEngineLifecycleComponent implements IRdbTenantComponent {

    public RdbTenantComponent() {
	super(LifecycleComponentType.DataStore);
    }
}
