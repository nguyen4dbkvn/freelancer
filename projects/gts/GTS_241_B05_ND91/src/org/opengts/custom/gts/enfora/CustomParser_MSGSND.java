// ----------------------------------------------------------------------------
// Copyright 2007-2012, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// This source module is PROPRIETARY and CONFIDENTIAL.
// NOT INTENDED FOR PUBLIC RELEASE.
// 
// Use of this software is subject to the terms and conditions outlined in
// the 'Commercial' license provided with this software.  If you did not obtain
// a copy of the license with this software please request a copy from the
// Software Provider.
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Change History:
//  2011/01/28  Martin D. Flynn
//     -Initial release
// ----------------------------------------------------------------------------
package org.opengts.custom.gts.enfora;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;

import org.opengts.dbtypes.*;

import org.opengts.db.*;
import org.opengts.db.tables.*;

public class CustomParser_MSGSND
    implements CustomParser
{

    // ------------------------------------------------------------------------

    /**
    *** Default Constructor
    **/
    public CustomParser_MSGSND()
    {
        // init
    }

    // ------------------------------------------------------------------------

    /**
    *** Callback to parse raw data received from a remote tracking device 
    *** through its device communication server.
    *** @param account  The assigned device Account instance
    *** @param device   The addigned Device instance
    *** @param type     The type of "data"
    *** @param data     The byte array containing the raw data
    *** @param props    A map where parsed data should be placed 
    ***                 (to be inserted into the EventData record)
    *** @return The response which will be sent back to the device
    **/
    public byte[] parseData(Account account, Device device, 
        String type, byte data[], 
        Map<String,Object> props)
    {

        /* "isTCP" will be true if TCP, false if UDP */
        boolean isTCP = StringTools.parseBoolean(props.get(CustomParser.DUPLEX),false);

        /* "str" below will contains the "$MSGSND" data as a String */
        if ("$MSGSND".equalsIgnoreCase(type)) {
            String MSGSND = StringTools.toStringValue(data);
        } else
        if ("USRVAR".equalsIgnoreCase(type)) {
            Payload p = new Payload(data);
            long USRVAR[] = new long[10];
            for (int i = 0; i < USRVAR.length; i++) {
                USRVAR[i] = p.readULong(4,0L);
            }
        }

        // Handle parsing of 'data' and fill "props" map as required
        // IE.
        //  props.put(EventData.FLD_timestamp , new Long   (timestamp ));
        //  props.put(EventData.FLD_statusCode, new Integer(statusCode));
        //  props.put(EventData.FLD_latitude  , new Double (latitude  ));
        //  props.put(EventData.FLD_longitude , new Double (longitude ));
        //  ...

        return null;
    }

    // ------------------------------------------------------------------------

}
