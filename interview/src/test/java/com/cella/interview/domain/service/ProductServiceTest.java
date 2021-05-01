package com.cella.interview.domain.service;

import com.cella.interview.domain.model.Product;
import com.cella.interview.domain.repository.ProductRepository;
import com.cella.interview.domain.service.impl.ProductServiceImpl;
import com.cella.interview.exception.EntityNotDeletedException;
import com.cella.interview.exception.EntityNotFoundException;
import com.cella.interview.exception.EntityNotSavedException;
import com.cella.interview.exception.GenericException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.PageRequest.of;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;

	private Product product = new Product(-1L, "Mesa", "De luz", 12.45d, 20.10d, "ARG");

	@BeforeEach
	void setup() {
	}

	@Nested
	class CreateTest {

		@Test
		@DisplayName("Should save and return the persisted entity")
		void createAndReturnProduct() throws GenericException {
			Product resultProduct = new Product();

			when(productRepository.save(any(Product.class))).thenReturn(resultProduct);

			Product result = productService.create(product);
			assertEquals(resultProduct, result);
		}

		@Test
		@DisplayName("Should throw exception when not returning persisted entity")
		void createAndReturnNull() throws GenericException {

			when(productRepository.save(any(Product.class))).thenReturn(null);

			assertThrows(EntityNotSavedException.class, () -> productService.create(product));
		}
	}

	@Nested
	class UpdateTest {

		@Test
		@DisplayName("Should save and return the updated entity")
		void updateAndReturnProduct() throws GenericException {

			Product resultProduct = new Product();
			when(productRepository.existsById(anyLong())).thenReturn(true);
			when(productRepository.save(any(Product.class))).thenReturn(resultProduct);

			Assertions.assertEquals(resultProduct, productService.update(product));
		}

		@Test
		@DisplayName("Should throw exception when not reurning updated entity")
		void updateAndReturnNull() throws GenericException {

			when(productRepository.existsById(anyLong())).thenReturn(true);
			when(productRepository.save(product)).thenReturn(null);

			assertThrows(EntityNotSavedException.class, () -> productService.update(product));
		}

		@Test
		@DisplayName("Should throw exception when cant find entity to update")
		void updateCantFindProduct() throws GenericException {

			when(productRepository.existsById(anyLong())).thenReturn(false);

			assertThrows(EntityNotFoundException.class, () -> productService.update(product));
		}
	}

	@Nested
	class DeleteTest {

		@Test
		@DisplayName("Should remove the entity from repository")
		void deleteProduct() throws GenericException {

			when(productRepository.existsById(anyLong())).thenReturn(true);
			doNothing().when(productRepository).deleteById(anyLong());
			productService.delete(-1L);
			verify(productRepository, times(1)).deleteById(anyLong());
		}

		@Test
		@DisplayName("Should throw exception if error during delete")
		void errorDuringDelete() throws GenericException {

			when(productRepository.existsById(anyLong())).thenReturn(true);
			doThrow(new RuntimeException()).when(productRepository).deleteById(anyLong());

			assertThrows(EntityNotDeletedException.class, () -> productService.delete(-1L));
		}

		@Test
		@DisplayName("Should throw exception when cant find entity to delete")
		void deleteCantFindProduct() throws GenericException {

			when(productRepository.existsById(anyLong())).thenReturn(false);

			assertThrows(EntityNotFoundException.class, () -> productService.delete(-1L));
		}
	}

	@Nested
	class GetTest {

		@Test
		@DisplayName("Should return all the entitites")
		void getAllProducts() throws GenericException {

			List<Product> products = new ArrayList<>();

			when(productRepository.findAll()).thenReturn(products);

			assertEquals(products, productService.getAll());
		}

		@Test
		@DisplayName("Should return entity")
		void getProductById() throws GenericException {

			Product resultProduct = new Product();
			when(productRepository.findById(anyLong())).thenReturn(Optional.of(resultProduct));

			Assertions.assertEquals(resultProduct, productService.getById(-1L));
		}

		@Test
		@DisplayName("Should throw exception when cant find entity")
		public void getByIdCantFindProduct() throws GenericException {

			when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> productService.getById(-1L));
		}

		@Test
		@DisplayName("Should return entity")
		void getProductByName() throws GenericException {

			Product resultProduct = new Product();
			when(productRepository.getByName(anyString())).thenReturn(Optional.of(resultProduct));

			Assertions.assertEquals(resultProduct, productService.getByName("Mesa"));
		}

		@Test
		@DisplayName("Should throw exception when cant find entity")
		public void getByNameCantFindProduct() throws GenericException {

			when(productRepository.getByName(anyString())).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> productService.getByName("Mesa"));
		}



		@Test
		@DisplayName("Should return all the entitites sorted by name Asc")
		void getSortedByNameAscProducts() throws GenericException {

			List<Product> products = new ArrayList<>();

			when(productRepository.findAll(Sort.by("name").ascending())).thenReturn(products);

			assertEquals(products, productService.getSortedByNameAscProducts());
		}

		@Test
		@DisplayName("Should return all the entitites sorted by name Desc")
		void getSortedByNameDescProducts() throws GenericException {

			List<Product> products = new ArrayList<>();

			when(productRepository.findAll(Sort.by("name").descending())).thenReturn(products);

			assertEquals(products, productService.getSortedByNameDescProducts());
		}

		@Test
		@DisplayName("Should return paginated entitites")
		void getPaginatedProducts() throws GenericException {

			Page<Product> products = new PageImpl<>(new ArrayList<>());

			when(productRepository.findAll(of(1, 2))).thenReturn(products);

			assertEquals(products, productService.getPaginatedProducts(1,2));
		}

		@Test
		@DisplayName("Should fail finding paginated entitites")
		void getPaginatedProductsNotFound() throws GenericException {

			Page<Product> products = new PageImpl<>(new ArrayList<>());

			when(productRepository.findAll(of(10, 10))).thenReturn(products);

			assertThrows(EntityNotFoundException.class, () -> productService.getPaginatedProducts(10,10));
		}
	}
}
