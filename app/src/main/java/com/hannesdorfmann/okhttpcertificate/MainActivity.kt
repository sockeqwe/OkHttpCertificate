package com.hannesdorfmann.okhttpcertificate

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.net.InetAddress
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        thread { // Don't run it on the main thread because of InetAddress.getByName()

            val localhostCertificate = HeldCertificate.Builder()
            .addSubjectAlternativeName(InetAddress.getByName("localhost").canonicalHostName)
            .build()


        val mockWebServer = MockWebServer()
        val serverCertificates = HandshakeCertificates.Builder()
            .heldCertificate(localhostCertificate)
            .build()

        mockWebServer.useHttps(serverCertificates.sslSocketFactory(), false)

        mockWebServer.start()

        val okHttp = OkHttpClient.Builder()
            .also {
                val clientCertificates = HandshakeCertificates.Builder()
                    .addTrustedCertificate(localhostCertificate.certificate())
                    .build()

                it.sslSocketFactory(
                    clientCertificates.sslSocketFactory(),
                    clientCertificates.trustManager()
                )

            }
            .build()
        }


    }



}
