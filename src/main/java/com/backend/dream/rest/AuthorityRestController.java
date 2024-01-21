package com.backend.dream.rest;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.dto.AuthorityDTO;
import com.backend.dream.entity.Authority;
import com.backend.dream.repository.AccountRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.repository.AuthorityRepository;
import com.backend.dream.service.AuthorityService;
import com.backend.dream.util.ExcelUltils;
import com.backend.dream.util.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/authorities")
public class AuthorityRestController {
	@Autowired
	AuthorityService authorityService;
	@Autowired
	AuthorityRepository authorityRepository;
	@Autowired
	private AccountService accountService;

	@GetMapping()
	public List<Authority> getAuthorities(@RequestParam("admin") Optional<Boolean> admin) {
		if (admin.orElse(false)) {
			return authorityService.getAdmin();
		} else {
			return authorityService.findALL();
		}
	}

	@PostMapping()
	public Authority authority(@RequestBody Authority authority) {
		return authorityService.create(authority);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		authorityService.delete(id);
	}

	@GetMapping("/searchAccounts")
	public List<AccountDTO> searchAccountsByName(@RequestParam("name") String name) {
		return accountService.searchAccount(name);
	}

	@GetMapping("/filterByRole")
	public List<AccountDTO> getUsersByRole(@RequestParam("roleID") Long roleID) {
		return accountService.getUsersByRole(roleID);
	}

	@GetMapping("/download")
	private ResponseEntity<InputStreamResource> download() throws IOException {
		String fileName = "Data-authorities.xlsx";
		ByteArrayInputStream inputStream = authorityService.getdataAuthority();
		InputStreamResource response = new InputStreamResource(inputStream);

		ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(response);
		return responseEntity;
	}

	@GetMapping("/pdf")
	public ResponseEntity<byte[]> exportToPdf() {
		try {
			List<Authority> authorities = authorityRepository.findAll();
			String title = "Data Authoriry";
			ByteArrayInputStream pdfStream = PdfUtils.dataToPdf(authorities, ExcelUltils.HEADERAUTHORITY, title);

			byte[] pdfContents = new byte[pdfStream.available()];
			pdfStream.read(pdfContents);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Data-Authorities.pdf");
			headers.setCacheControl("must-revalidate, no-store");

			return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
