/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package io.confluent.connect.hdfs.avro;

import io.confluent.connect.hdfs.FileMerger;
import io.confluent.connect.hdfs.storage.HdfsStorage;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class AvroFileAppender implements FileMerger {

  private final Configuration conf;
  private final HdfsStorage storage;

  // TODO: Change HdfsStorage to Storage, once the "copy" method is available
  public AvroFileAppender(Configuration conf, HdfsStorage storage) {
    this.conf = conf;
    this.storage = storage;
  }

  @Override
  public void append(String oldContentFile,
                     String newContentFile,
                     String fileToMergeTo) throws IOException {

    storage.copy(oldContentFile, fileToMergeTo, true);

    final Path newContentFilePath = new Path(newContentFile);
    final Path fileToMergeToPath = new Path(fileToMergeTo);

    final DatumWriter<Object> datumWriter = new GenericDatumWriter<>();
    final DataFileWriter<Object> writer = new DataFileWriter<>(datumWriter);

    final DatumReader<Object> datumReader = new GenericDatumReader<>();

    final FsInput inNew = new FsInput(newContentFilePath, conf);
    final FsInput inFileToMerge = new FsInput(fileToMergeToPath, conf);

    final DataFileReader<Object> reader = new DataFileReader<>(inNew, datumReader);

    final FSDataOutputStream out = fileToMergeToPath.getFileSystem(conf).append(fileToMergeToPath);

    writer.appendTo(inFileToMerge, out);

    writer.appendAllFrom(reader, false);

    writer.close();
  }
}
