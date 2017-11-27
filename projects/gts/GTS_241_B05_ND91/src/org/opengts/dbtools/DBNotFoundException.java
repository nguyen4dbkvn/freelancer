// ----------------------------------------------------------------------------
// Copyright 2007-2012, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Change History:
//  2008/05/14  Martin D. Flynn
//     -Initial release
// ----------------------------------------------------------------------------
package org.opengts.dbtools;

import java.lang.*;
import java.util.*;
import java.sql.*;

import org.opengts.util.*;

/**
*** <code>DBNotFoundException</code> is thrown in cases where a record was expected,
*** but no record was found.
**/

public class DBNotFoundException
    extends DBException
{
    
    // ----------------------------------------------------------------------------

    /**
    *** Constructor
    *** @param msg  The message associated with this exception
    **/
    public DBNotFoundException(String msg)
    {
        super(msg);
    }

    /**
    *** Constructor
    *** @param msg   The message associated with this exception
    *** @param cause The cause of this exception
    **/
    public DBNotFoundException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    // ----------------------------------------------------------------------------

}