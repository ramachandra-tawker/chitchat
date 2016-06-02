<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use DB;

use Log;


class Contact extends Controller
{
    public function resolveContacts(Request $request)
    {
    	$data = $request->getContent();
    	$phone_numbers = json_decode($data)->numbers; //taking only numbers out of data (key valur pair)
    	
    	//if number exist 
    	$contactsresolve = isset($phone_numbers)?count($phone_numbers):0;

    	if ($contactsresolve > 0) {
    		$contacts = DB::table('user')
		    		->select('username','status','lastseen')
		    		->whereIn('phone_number', $phone_numbers)
		    		->get();
	    	}

		$result['contacts']=$contacts;

		return response()->json($result);


    }
}
