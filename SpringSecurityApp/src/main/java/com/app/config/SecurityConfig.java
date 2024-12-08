package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration // Para decirle a spring que es una clase de configuration
@EnableWebSecurity// Para habilitar la seguridad web
@EnableMethodSecurity // Nos permite hacer alguna configuracion con annotacion propoas de ss
public class SecurityConfig {



    //El primer componentes que tenemos que configurar es
    //Aca nosotros vamos a crear nuestras condiciones personalizadas , aca debemos de definirlas

    //@Bean
    //public SecurityFilterChain  securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    //    return httpSecurity
    //            /*CSRF Si trabajamos con rest ,no lo necesitamos , pero si trabajamos con MVC si lo necesitamos*/
    //            .csrf(csrf -> csrf.disable())/*La configuracion esta desabilidata */
    //            .httpBasic(Customizer.withDefaults())/*SOLO SE USA cuando nos vamos a loguear con usuario y contraselña , cuando nos logueamos con toque se maneja de una manera diferente*/
    //            /*Un STATELESS quiere decir que nos guardaremos esa session en memoria , si no que el tiempo del usuario loguedao
    //            * va a dependeer de la duracion del token , cuando trabajamos con el token*/
    //            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //            /*Aora debemos de configurar los endPoints publico  y privados*/
    //            .authorizeHttpRequests(http -> {
    //                /*Aca adentro configuramos el comportamiento    */
    //                http.requestMatchers(HttpMethod.GET,"/auth/hello").permitAll();/*sera publico */
    // Cofnigurar los endpoints privados
    //                http.requestMatchers(HttpMethod.POST, "/auth/post").hasAnyRole("ADMIN", "DEVELOPER");
    //                http.requestMatchers(HttpMethod.PATCH, "/auth/patch").hasAnyAuthority("REFACTOR");
    //                /*Si se hace una peticion a este endpoint deber  de tener una autorizaciom   */
    //                http.requestMatchers(HttpMethod.GET,"/auth/hello-secured").hasAnyAuthority("CREATE");
    //                //Configurar el resto de endpoint -NO ESPECIFICADO
    //                //http.anyRequest().authenticated();->esto queire decir que tendre acceso a caulquier endpoints que no este especificado arriba , pero siempre y cuando este autenticado
    //                http.anyRequest().denyAll();//rechazatodo lo que no se identifique
    //            })
    //           .build();

    // }




    /*Trabajar con anotaciones*/
    /*vamos a borrar authorizeHttpRequests*/
    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .build();

        /*las condiciones se configuran en el controler*/

     }

    //Este AuthenticationManager podra tener mucho proveedores , pero en este caso usaremos un proveedor que nos
    //permita conetarnos ala base dedatos
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return  authenticationConfiguration.getAuthenticationManager();
    }


    //Necesitamos un proveedor de autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        /*Ocuparemos este proveedor de autenticacion porque este nos permiste conectarnos a la base dedatos  para traer los usarios*/
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        /*Para continuar debemos de tener 2 componentes mas*/

        provider.setPasswordEncoder(passwordEncoder());/*Este componente lo que hace ebcripta las contraseñas y valida las contraseñas */
        provider.setUserDetailsService(userDetailsService);/*Este componente hace el llamado ala base de datos*/
        return provider;
    }

    /*Esto es la simulacion de la conexion ala base dedatos  , estamos simulando que estamos extraendo los datos de un usuario*/
    /*@Bean
    public UserDetailsService userDetailsService(){
        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("Luis")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ","CREATE")
                .build());

        userDetailsList.add(User.withUsername("Vanne")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ")
                .build());


        return new InMemoryUserDetailsManager(userDetailsList);
    }*/

    public PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
//Para generar la incriptacion
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }
}
