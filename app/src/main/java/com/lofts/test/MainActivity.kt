package com.lofts.test

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.lofts.test.Utils.PermissionManager
import com.lofts.test.Utils.PoiUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_word.setOnClickListener { getExternalPermission() }
    }

    private fun getExternalPermission() {
        PermissionManager.with(this)
            .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : PermissionManager.OnRequestPermission {
                override fun requestSuccess(granted: List<String>, isAll: Boolean) {
                    createWord()
                }

                override fun resuestFailure(granted: List<String>, isAll: Boolean) {
                    Toast.makeText(this@MainActivity, "存储权限禁止", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun createWord() {
        val map = HashMap<String, Any>()
        map["\${caseNum}"] = "20162314"
        map["\${party}"] = "李三"
        map["\${address}"] = "梨园市人民路"
        map["\${sinBehaviour}"] = "非法干扰他人"
        map["\${according}"] = "目击人投诉举报"
        map["\${sinLaw}"] = "第三条"
        map["\${punishLaw}"] = "第一条"
        map["\${punishResult}"] = "罚款2000元"
        map["\${createDate}"] = "2016-12-14"


        val sealPath = cacheDir.path + "/seal.png"
        if (!TextUtils.isEmpty(sealPath)) {
            val sealMap = HashMap<String, Any>()
            sealMap["width"] = 100
            sealMap["height"] = 100
            sealMap["content"] = sealPath
            map["\${seal}"] = sealMap
        } else {
            map["\${seal}"] = "  "
        }

        val fileName = System.currentTimeMillis().toString() + ".docx"
        val newFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName
        try {
            //读取word文档模版
            val isStream = assets.open("test.docx")
            val doc = XWPFDocument(isStream)
            //解析替换对应的字段
            PoiUtil.parseDocx(doc, map)
            doc.write(FileOutputStream(newFile))

            isStream.close()
            //打开word文档准备打印
            PoiUtil.openDocx(this, File(newFile))
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}
