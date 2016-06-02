<?php

namespace App\Http\Controllers;

use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Routing\Controller as BaseController;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Auth\Access\AuthorizesResources;
use Illuminate\Http\Request;
use DB;
use Log;

class Login extends BaseController
{
    //use AuthorizesRequests, AuthorizesResources, DispatchesJobs, ValidatesRequests;

    public function login(Request $request){
    	Log::info('new login attempt');
    	$email = $request->input('email');
    	$password = $request->input('password');
    	$name = DB::table('user')->where('username',$email)
							    	->where('password',$password)
							    	->value('nickname');

		$result['response']=false;
		if(isset($name)){
			Log::info('successful');
			$result['response']=true;
			$result['nickname']=$name;
		}
		Log::info('fail');
		return response()->json($result);
		
    }
}

