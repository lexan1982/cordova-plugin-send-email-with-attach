<!--
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#  KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#
-->

cordova-plugin-send-email-with-attach
------------------------

This is a plugin implementation of the <b>Emailer</b> function which call native Email sendler and can pass parametrs with file path which will be attached to the mail. 

------------------------
Call function <b>cordova.emailer</b> from js code with params:

  <pre>cordova.emailer(callback, error, mail);</pre>
  
  <i>callback</i> - success function<br/>
  <i>error</i> - error function<br/>
  <i>mail</i> - json object: 
  <pre>{ mail: 'mail@mail.com', subject: 'Report', text: 'Hello.<br/>Calendar plan in attach.', attachPath: 'file:///mnt/card/fileName' }</pre>
  
  <a href="http://cordova.apache.org/docs/en/3.5.0/guide_hybrid_plugins_index.md.html#Plugin%20Development%20Guide">Cordova docs</a>
