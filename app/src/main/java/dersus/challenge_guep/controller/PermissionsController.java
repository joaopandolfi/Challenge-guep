package dersus.challenge_guep.controller;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import dersus.challenge_guep.constants.BundleConstants;

/**
 * Created by joao on 31/05/18.
 * Classe de controle das permissoes requeridas pelo APP
 */

public class PermissionsController {

    protected Activity activity;

    /*
    * Verifica se as permissoes foram concedidas
    * @params actitity {Activity} (Ponteiro da acticvity que está requerendo a permissao)
    * @params permissions {String[]} (Array de permissoes requeridas)
    * @returns okPermissions {boolean} (Retorna verdadeiro se todas permissoes foram concedidas)
    * */
    public boolean checkPermissions(Activity activity,String[] permissions){
        this.activity = activity;
        boolean flag = true;

        //VERIFICANDO E PEDINDO PERMISSOES
        for(String permission: permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                flag = false;
                break;
            }
        }

        if(flag)
            return true;
        else
            ActivityCompat.requestPermissions(activity, permissions, BundleConstants.RESPONSE_INTENT_PERMISSIONS);

        return false;
    }

}
