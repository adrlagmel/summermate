/* AlmacenS3.java
 *
 * Imágenes en un cubo compatible con S3. Con Cloudflare R2:
 *   IMAGENES_ALMACEN=s3
 *   S3_ENDPOINT=https://<id de cuenta>.r2.cloudflarestorage.com
 *   S3_BUCKET=summermate-imagenes
 *   S3_REGION=auto
 *   S3_ACCESS_KEY / S3_SECRET_KEY   (token de API de R2 con permiso sobre el cubo)
 *   S3_URL_PUBLICA=https://imagenes.tudominio.es   (opcional: dominio público del cubo)
 * Las claves de acceso solo se leen de variables de entorno, nunca del repositorio.
 */

package services.imagenes;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Component
@ConditionalOnProperty(name = "summermate.imagenes.almacen", havingValue = "s3")
public class AlmacenS3 implements AlmacenImagenes, DisposableBean {

	private final S3Client	s3;
	private final String	bucket;
	private final String	urlPublica;


	public AlmacenS3(@Value("${summermate.imagenes.s3.endpoint}") String endpoint, @Value("${summermate.imagenes.s3.bucket}") String bucket,
		@Value("${summermate.imagenes.s3.region:auto}") String region, @Value("${summermate.imagenes.s3.access-key}") String accessKey,
		@Value("${summermate.imagenes.s3.secret-key}") String secretKey, @Value("${summermate.imagenes.s3.url-publica:}") String urlPublica) {
		this.bucket = bucket;
		this.urlPublica = urlPublica.endsWith("/") ? urlPublica.substring(0, urlPublica.length() - 1) : urlPublica;
		this.s3 = S3Client.builder()
			.endpointOverride(URI.create(endpoint))
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
			// Rutas del tipo endpoint/cubo/clave: las aceptan R2 y los simuladores de S3
			.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
			.build();
	}

	@Override
	public void guardar(String clave, byte[] datos, String tipo) {
		s3.putObject(b -> b.bucket(bucket).key(clave).contentType(tipo)
			// Las claves no se reutilizan nunca: se puede cachear para siempre
			.cacheControl("public, max-age=31536000, immutable"), RequestBody.fromBytes(datos));
	}

	@Override
	public byte[] leer(String clave) {
		return s3.getObjectAsBytes(b -> b.bucket(bucket).key(clave)).asByteArray();
	}

	@Override
	public void borrar(String clave) {
		s3.deleteObject(b -> b.bucket(bucket).key(clave));
	}

	@Override
	public Optional<String> urlPublica(String clave) {
		return urlPublica.isBlank() ? Optional.empty() : Optional.of(urlPublica + "/" + clave);
	}

	@Override
	public void destroy() {
		s3.close();
	}

}
