/*
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

package org.apache.shardingsphere.mode.metadata.refresher.type.schema;

import com.google.common.base.Preconditions;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.database.schema.pojo.AlterSchemaPOJO;
import org.apache.shardingsphere.mode.metadata.refresher.MetaDataRefresher;
import org.apache.shardingsphere.mode.persist.service.MetaDataManagerPersistService;
import org.apache.shardingsphere.sql.parser.statement.core.statement.ddl.AlterSchemaStatement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Schema refresher for alter schema statement.
 */
public final class AlterSchemaStatementSchemaRefresher implements MetaDataRefresher<AlterSchemaStatement> {
    
    @Override
    public void refresh(final MetaDataManagerPersistService metaDataManagerPersistService, final ShardingSphereDatabase database, final Collection<String> logicDataSourceNames,
                        final String schemaName, final DatabaseType databaseType, final AlterSchemaStatement sqlStatement, final ConfigurationProperties props) throws SQLException {
        Optional<String> renameSchemaName = sqlStatement.getRenameSchema().map(optional -> optional.getValue().toLowerCase());
        Preconditions.checkArgument(renameSchemaName.isPresent(), String.format("The renamed schema is not exist of schema '%s'.", schemaName));
        metaDataManagerPersistService.alterSchema(new AlterSchemaPOJO(database.getName(), sqlStatement.getSchemaName().getValue().toLowerCase(),
                renameSchemaName.get(), logicDataSourceNames));
    }
    
    @Override
    public Class<AlterSchemaStatement> getType() {
        return AlterSchemaStatement.class;
    }
}
